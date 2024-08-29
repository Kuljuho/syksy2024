import { StatusBar } from 'expo-status-bar';
import { useState, useEffect } from 'react';
import { Button, StyleSheet, TextInput, Text, View } from 'react-native';

export default function App() {
  const [age, setAge] = useState('')
  const [heartrate, setHeartrate] = useState('')

  const calculate = () => {
    const numericAge = parseInt(age);
    if (!isNaN(numericAge)) {
      const lower = (220 - age) * 0.65
      const upper = (220 - age) * 0.85
    setHeartrate(`${lower.toFixed(0)} - ${upper.toFixed(0)}`)
    } else {
      setHeartrate('Invalid age')
    }
  }
  
  return (
    <View style={styles.container}>
      <Text style={styles.field}>Age</Text>
      <TextInput 
        style={styles.field}
        placeholder='Age...'
        value={age}
        onChangeText={text => setAge(text)}
        keyBoardType='decimal-pad'
      />
      <Text style={styles.field}>Limits</Text>
      <Text style={styles.field}>{heartrate}</Text>
      <Button title='Calculate' onPress={calculate}></Button>
      <StatusBar style="auto" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'flex-start',
    paddingTop: 20,
    margin: 8,
  },
  field: {
    marginTop: 8,
    marginBottom: 8,
  }
});
