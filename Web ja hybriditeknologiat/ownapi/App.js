import React from 'react';
import { View } from 'react-native';
import { StatusBar } from 'expo-status-bar';
import MemeGenerator from './components/MemeGenerator';
import styles from './styles/styles';

export default function App() {
  return (
    <View style={styles.container}>
      <MemeGenerator />
      <StatusBar style="auto" />
    </View>
  );
}
