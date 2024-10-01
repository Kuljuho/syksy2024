import React from "react";
import { View, Text, StyleSheet } from "react-native";
import { Picker } from "@react-native-picker/picker";

export default function Settings({ mapType, setMapType }) {
  return (
    <View style={styles.settingsArea}>
      <Text style={styles.heading}>Map Type</Text>
      <Picker
        selectedValue={mapType}
        onValueChange={(itemValue) => setMapType(itemValue)}
      >
        <Picker.Item label="Standard" value="standard" />
        <Picker.Item label="Satellite" value="satellite" />
        <Picker.Item label="Hybrid" value="hybrid" />
      </Picker>
    </View>
  );
}

const styles = StyleSheet.create({
  settingsArea: {
    marginTop: 32,
    marginLeft: 16,
  },
  heading: {
    textTransform: "uppercase",
    marginBottom: 10,
  },
});
