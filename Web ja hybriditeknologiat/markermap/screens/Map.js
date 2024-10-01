import React from "react";
import { StyleSheet, View, Text, Dimensions } from "react-native";
import MapView, { Marker } from "react-native-maps";

export default function Map({
  location,
  setLocation,
  mapType,
  markers,
  setMarkers,
}) {
  if (!location) {
    return <Text>Paina paikannusnappia kartan yläreunassa</Text>;
  }

  const addMarker = (e) => {
    const coords = e.nativeEvent.coordinate;
    setMarkers([...markers, coords]);
  };

  return (
    <View style={styles.container}>
      <MapView
        style={styles.map}
        initialRegion={location}
        onRegionChangeComplete={setLocation}
        onLongPress={addMarker}
        mapType={mapType}
      >
        {markers.map((marker, index) => (
          <Marker
            key={index}
            coordinate={marker}
            title={`Marker ${index + 1}`}
          />
        ))}
      </MapView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  map: {
    width: Dimensions.get("window").width,
    height: Dimensions.get("window").height,
  },
});
