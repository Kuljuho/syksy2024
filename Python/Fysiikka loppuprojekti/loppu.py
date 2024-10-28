import streamlit as st
import pandas as pd
import numpy as np
from scipy import signal
from scipy.fft import fft, fftfreq
import folium
from streamlit_folium import folium_static
import plotly.express as px
import plotly.graph_objects as go

st.title('Kävelyn analyysi')

@st.cache_data
def load_data():
    acc_data = pd.read_csv('Linear Acceleration.csv')
    loc_data = pd.read_csv('Location.csv')
    
    acc_data = acc_data[acc_data['Time (s)'] > 20]
    loc_data = loc_data[loc_data['Time (s)'] > 20]
    
    acc_data['Time (s)'] = acc_data['Time (s)'] - acc_data['Time (s)'].min()
    loc_data['Time (s)'] = loc_data['Time (s)'] - loc_data['Time (s)'].min()
    
    return acc_data, loc_data

def filter_acc(data):
    fs = 1/(data['Time (s)'].iloc[1] - data['Time (s)'].iloc[0])
    nyq = fs/2
    low = 0.5/nyq
    high = 3.0/nyq
    b, a = signal.butter(4, [low, high], btype='band')
    filtered = signal.filtfilt(b, a, data['Linear Acceleration y (m/s^2)'])
    return filtered

def count_steps(filtered_data):
    peaks, _ = signal.find_peaks(filtered_data, height=0.5, distance=20)
    return len(peaks)

def fourier_analysis(data):
    N = len(data)
    T = data['Time (s)'].iloc[1] - data['Time (s)'].iloc[0]
    
    acc = data['Linear Acceleration y (m/s^2)'].values
    acc = acc - np.mean(acc)

    yf = fft(acc)
    xf = fftfreq(N, T)
    
    psd = np.abs(yf)**2 / N

    positive_freq_mask = xf > 0
    freq = xf[positive_freq_mask]
    psd = psd[positive_freq_mask]
    
    return freq, psd

def calculate_distance(lat1, lon1, lat2, lon2):
    from math import sin, cos, sqrt, atan2, radians
    R = 6371e3
    
    lat1, lon1, lat2, lon2 = map(radians, [lat1, lon1, lat2, lon2])
    
    dlat = lat2 - lat1
    dlon = lon2 - lon1
    
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * atan2(sqrt(a), sqrt(1-a))
    
    return R * c

acc_data, loc_data = load_data()
filtered_acc = filter_acc(acc_data)
steps_filtered = count_steps(filtered_acc)
freq, psd = fourier_analysis(acc_data)
main_freq = freq[np.argmax(psd)]
steps_fourier = int(main_freq * (acc_data['Time (s)'].max() - acc_data['Time (s)'].min()))

total_distance = 0
for i in range(len(loc_data)-1):
    lat1 = loc_data['Latitude (°)'].iloc[i]
    lon1 = loc_data['Longitude (°)'].iloc[i]
    lat2 = loc_data['Latitude (°)'].iloc[i+1]
    lon2 = loc_data['Longitude (°)'].iloc[i+1]
    total_distance += calculate_distance(lat1, lon1, lat2, lon2)

total_time = loc_data['Time (s)'].iloc[-1] - loc_data['Time (s)'].iloc[0]
average_speed = total_distance / total_time
step_length = (total_distance / steps_filtered) * 100

st.header('Analyysin tulokset')

col1, col2, col3 = st.columns(3)
with col1:
    st.metric(
        "Askelmäärät", 
        f"{steps_filtered} / {steps_fourier}",
        "Suodatus / Fourier"
    )
with col2:
    st.metric(
        "Nopeus ja matka", 
        f"{average_speed:.2f} m/s",
        f"Matka: {total_distance/1000:.2f} km"
    )
with col3:
    st.metric(
        "Askelpituus",
        f"{step_length:.1f} cm",
        "Keskiarvo"
    )

st.markdown("---")

st.subheader('Kiihtyvyyden komponenttien vertailu')

fig_components = go.Figure()

offset = 2

fig_components.add_trace(go.Scatter(
    x=acc_data['Time (s)'],
    y=acc_data['Linear Acceleration z (m/s^2)'],
    name='Z-komponentti',
    line=dict(color='green', width=2)
))

fig_components.add_trace(go.Scatter(
    x=acc_data['Time (s)'],
    y=acc_data['Linear Acceleration x (m/s^2)'] + 2*offset,
    name='X-komponentti',
    line=dict(color='blue', width=2)
))

fig_components.add_trace(go.Scatter(
    x=acc_data['Time (s)'],
    y=acc_data['Linear Acceleration y (m/s^2)'] + offset,
    name='Y-komponentti',
    line=dict(color='red', width=2)
))

fig_components.update_layout(
    title='Kiihtyvyyden komponentit',
    xaxis_title="Aika (s)",
    yaxis_title="Kiihtyvyys (m/s²)",
    legend_title="Komponentti",
    showlegend=True,
    hovermode='x unified'
)

st.plotly_chart(fig_components)

st.markdown("""
Y-komponentti (punainen) valittiin analyysiin, koska siinä kävelyliike näkyy selkeimmin 
säännöllisinä heilahduksina.
""")

st.subheader('Suodatettu kiihtyvyysdata')
filtered_df = pd.DataFrame({
    'Time': acc_data['Time (s)'],
    'Filtered Acceleration': filtered_acc
})
fig_filtered = px.line(filtered_df, x='Time', y='Filtered Acceleration',
                      title='Suodatettu kiihtyvyysdata (y-komponentti)')
fig_filtered.update_layout(
    xaxis_title="Aika (s)",
    yaxis_title="Kiihtyvyys (m/s²)"
)
st.plotly_chart(fig_filtered)

st.subheader('Tehospektritiheys')
L = freq <= 14
chart_data = pd.DataFrame(np.transpose(np.array([freq[L],psd[L].real])), columns=["freq", "psd"])
st.line_chart(chart_data, x = 'freq', y = 'psd', y_label = 'Teho', x_label = 'Taajuus [Hz]')

st.subheader('Reitti kartalla')
m = folium.Map(location=[loc_data['Latitude (°)'].mean(), 
                        loc_data['Longitude (°)'].mean()],
               zoom_start=15)

folium.Marker(
    [loc_data['Latitude (°)'].iloc[0], loc_data['Longitude (°)'].iloc[0]],
    popup='Lähtö',
    icon=folium.Icon(color='green', icon='info-sign')
).add_to(m)

folium.Marker(
    [loc_data['Latitude (°)'].iloc[-1], loc_data['Longitude (°)'].iloc[-1]],
    popup='Maali',
    icon=folium.Icon(color='red', icon='info-sign')
).add_to(m)

points = [[row['Latitude (°)'], row['Longitude (°)']] for _, row in loc_data.iterrows()]
folium.PolyLine(
    points, 
    weight=3, 
    color='blue', 
    opacity=0.8,
    popup=f'Kokonaismatka: {total_distance/1000:.2f} km'
).add_to(m)
folium_static(m)
