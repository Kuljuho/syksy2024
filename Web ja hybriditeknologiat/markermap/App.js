import { StatusBar } from "expo-status-bar";
import { StyleSheet, View, Platform } from "react-native";
import { useState, useEffect } from "react";
import { PaperProvider, IconButton } from "react-native-paper";
import Constants from "expo-constants";
import { SafeAreaView } from "react-native-safe-area-context";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import Map from "./screens/Map";
import Settings from "./screens/Settings";
import MainAppBar from "./components/MainAppBar";
import * as Location from "expo-location";

const Stack = createStackNavigator();

const settings = {
  backgroundColor: "#00a484",
};

const icons = {
  location_not_known: "crosshairs",
  location_searching: "crosshairs-question",
  location_found: "crosshairs-gps",
};

export default function App() {
  const [location, setLocation] = useState(null);
  const [icon, setIcon] = useState(icons.location_not_known);
  const [mapType, setMapType] = useState('standard');
  const [markers, setMarkers] = useState([]);

  const getUserPosition = async () => {
    setIcon(icons.location_searching);
    let { status } = await Location.requestForegroundPermissionsAsync();
    if (status !== "granted") {
      console.log("Sijainnin käyttöoikeutta ei myönnetty");
      setIcon(icons.location_not_known);
      return;
    }

    try {
      let location = await Location.getCurrentPositionAsync({});
      setLocation({
        latitude: location.coords.latitude,
        longitude: location.coords.longitude,
        latitudeDelta: 0.0922,
        longitudeDelta: 0.0421,
      });
      setIcon(icons.location_found);
    } catch (error) {
      console.log("Sijainnin hakeminen epäonnistui");
      setIcon(icons.location_not_known);
    }
  };

  return (
    <PaperProvider>
      <NavigationContainer>
        <Stack.Navigator
          initialRouteName="Map"
          screenOptions={{
            header: (props) => (
              <MainAppBar
                {...props}
                backgroundColor={settings.backgroundColor}
                icon={icon}
                getUserPosition={getUserPosition}
              />
            ),
          }}
        >
          <Stack.Screen name="Map">
            {(props) => (
              <Map
                {...props}
                location={location}
                setLocation={setLocation}
                mapType={mapType}
                markers={markers}
                setMarkers={setMarkers}
              />
            )}
          </Stack.Screen>
          <Stack.Screen name="Settings">
            {(props) => (
              <Settings
                {...props}
                mapType={mapType}
                setMapType={setMapType}
              />
            )}
          </Stack.Screen>
        </Stack.Navigator>
      </NavigationContainer>
    </PaperProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#fff",
    paddingTop: Platform.OS === "android" ? Constants.statusBarHeight : 0,
  },
});
