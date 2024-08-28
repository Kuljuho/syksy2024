import { StatusBar } from 'expo-status-bar';
import { useEffect, useState } from 'react';
import { Button, StyleSheet, Text, TextInput, View } from 'react-native';

export default function App() {
  const [euros, setEuros] = useState('')
  const [pounds, setPounds] = useState(0)

  useEffect(() => {
    const result = euros.replace(',','.') * 0.9
    setPounds(result)
  }, [euros])
  
  /*const calculate = () => {
    const result = euros.replace(',','.') * 0.9
    setPounds(result)
  }*/

  /*const calculate = (value) => {
    setEuros(value)
    const result = value.replace(',','.') * 0.9
    setPounds(result)
  }*/

  return (
    <View style={styles.container}>
      <Text style={styles.field}>Euros</Text>
      <TextInput
        style={styles.field}
        placeholder='Amount of euros...'
        value={euros}
        /*onChangeText={text => calculate(text)}*/
        onChangeText={text => setEuros(text)}
        keyBoardType='decimal-pad'
      />
      <Text style={styles.field}>Pounds</Text>
      <Text style={styles.field}>{pounds.toFixed(2)}</Text>
      {/*<Button title='Calculate' onPress={calculate}></Button>*/}
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
