import React from 'react';
import { View } from 'react-native';
import { Button, Text } from 'react-native-paper';

const SecondScreen = ({ navigation }) => {
  return (
    <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
      <Text>Second Screen</Text>
      <Button mode="contained" onPress={() => navigation.goBack()}>
        Go Back
      </Button>
    </View>
  );
};

export default SecondScreen;