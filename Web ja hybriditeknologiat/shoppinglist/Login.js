import { View, Text, TextInput, Button, StyleSheet } from "react-native";
import { useState } from "react";
import { auth, signInWithEmailAndPassword } from "./firebase/Config";

export default function Login({ setIsLoggedIn }) {
  const [userName, setUserName] = useState("joo@test.fi");
  const [password, setPassword] = useState("joojoo");

  const login = () => {
    signInWithEmailAndPassword(auth, userName, password)
      .then((userCredential) => {
        setIsLoggedIn(true);
      })
      .catch((error) => {
        if (
          error.code === "auth/wrong-password" ||
          error.code === "auth/user-not-found"
        ) {
          alert("Invalid username or password");
        } else if (error.code === "auth/too-many-requests") {
          alert("Too many requests, please try again later");
        } else {
          alert(error.code + " " + error.message);
        }
      });
  };

  return (
    <View style={styles.container}>
      <Text style={styles.header}>Login</Text>
      <TextInput
        style={styles.input}
        placeholder="Email"
        value={userName}
        onChangeText={setUserName}
      />
      <TextInput
        style={styles.input}
        placeholder="Password"
        value={password}
        onChangeText={setPassword}
        secureTextEntry
      />
      <Button title="Login" onPress={login} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    padding: 16,
  },
  header: {
    fontSize: 24,
    marginBottom: 16,
    textAlign: "center",
  },
  input: {
    height: 40,
    borderColor: "gray",
    borderWidth: 1,
    marginBottom: 12,
    paddingLeft: 8,
  },
});
