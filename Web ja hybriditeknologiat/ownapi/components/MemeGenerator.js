import React, { useState, useEffect } from 'react';
import { Text, View, Image, TextInput, TouchableOpacity } from 'react-native';
import { fetchTemplates, generateMeme } from '../services/memeService';
import styles from '../styles/styles';

export default function MemeGenerator() {
  const [topText, setTopText] = useState('');
  const [bottomText, setBottomText] = useState('');
  const [memeUrl, setMemeUrl] = useState('');
  const [templates, setTemplates] = useState([]);

  useEffect(() => {
    fetchTemplates().then(setTemplates).catch(console.error);
  }, []);

  const handleGenerateMeme = async () => {
    if (templates.length === 0) return;
    try {
      const url = await generateMeme(templates, topText, bottomText);
      setMemeUrl(url);
    } catch (error) {
      console.error('Virhe meemin luomisessa:', error);
    }
  };

  return (
    <View>
      <Text style={styles.title}>Meemigeneraattori</Text>
      <TextInput
        style={styles.input}
        placeholder="Yläteksti"
        value={topText}
        onChangeText={setTopText}
      />
      <TextInput
        style={styles.input}
        placeholder="Alateksti"
        value={bottomText}
        onChangeText={setBottomText}
      />
      <TouchableOpacity style={styles.button} onPress={handleGenerateMeme}>
        <Text style={styles.buttonText}>Luo satunnainen meemi</Text>
      </TouchableOpacity>
      {memeUrl ? (
        <Image source={{ uri: memeUrl }} style={styles.meme} />
      ) : (
        <Text style={styles.placeholder}>Meemi ilmestyy tähän</Text>
      )}
    </View>
  );
}