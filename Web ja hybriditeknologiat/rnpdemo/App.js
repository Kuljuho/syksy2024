import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';
import { Button, PaperProvider } from 'react-native-paper';
import MyAppbar from './components/MyAppbar';

export default function App() {

  return (
    <PaperProvider> 
    <MyAppbar />
    <View style={styles.container}>

      <Button mode="elevated">Press me</Button>
      <StatusBar style="auto" />
    </View>
    </PaperProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  text: {
    marginBottom: 48
  }
});
