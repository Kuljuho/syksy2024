import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv(r'D:\OAMK\Mobiiliohjelmointi\syksy2024\Python\Viikko 1 fysiikka\Acceleration without g 2024-09-08 17-20-33\Raw Data.csv')

print(data.head())

plt.figure(figsize=(10, 6))
plt.plot(data['Time (s)'], data['Linear Acceleration x (m/s^2)'], label='X')
plt.plot(data['Time (s)'], data['Linear Acceleration y (m/s^2)'], label='Y')
plt.plot(data['Time (s)'], data['Linear Acceleration z (m/s^2)'], label='Z')

plt.title('Puhelimen kiihtyvyys eri suuntiin')
plt.xlabel('Aika (s)')
plt.ylabel('Kiihtyvyys (m/s²)')
plt.legend()
plt.grid(True)
plt.show()