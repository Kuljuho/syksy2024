import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View, TextInput, ScrollView } from 'react-native';
import { useState, useEffect, useRef } from 'react'
import useAbortableFetch from './hooks/useAbortableFetch'

const URL = 'https://thecocktaildb.com/api/json/v1/1/search.php?s='

export default function App() {
  const [phrase, setPhrase] = useState('')
  const urlRef = useRef()
  const { data, error, loading } = useAbortableFetch(urlRef.current)

  const searchCocktails = (text) => {
    setPhrase(text)
    const address = URL + text
    urlRef.current = address
  }

  return (
    <View style={styles.container}>
      <View style={styles.searchBox}>
      <Text style={styles.heading}>Cocktails</Text>
      <TextInput
        style={styles.field}
        placeholder='Search for a cocktail'
        value={phrase}
        onChangeText={text => searchCocktails(text)}
      />
      </View>
      <ScrollView>
        {data !== null && data.drinks.map((drink, index) => (
          <Text key={drink.strDrink}>{drink.strDrink}</Text>
        ))}
      </ScrollView>
      <StatusBar style="auto" />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    paddingTop: 20,
    margin: 8,
  },
  heading: {
    fontSize: 40,
    marginTop: 16,
    marginBottom: 16,
  },
  field: {
    marginTop: 8,
    marginBottom: 16,
  }
});
