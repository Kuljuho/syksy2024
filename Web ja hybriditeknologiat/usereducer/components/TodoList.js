import React, { useReducer, useState } from "react";
import { StyleSheet, View, TextInput, Button, FlatList } from "react-native";
import TodoItem from "./TodoItem";

const initialState = { tasks: [] };

function reducer(state, action) {
  switch (action.type) {
    case "ADD_TASK":
      return { tasks: [...state.tasks, action.payload] };
    case "REMOVE_TASK":
      return {
        tasks: state.tasks.filter((_, index) => index !== action.payload),
      };
    default:
      return state;
  }
}

export default function TodoList() {
  const [state, dispatch] = useReducer(reducer, initialState);
  const [newTask, setNewTask] = useState("");

  const addTask = () => {
    if (newTask.trim()) {
      dispatch({ type: "ADD_TASK", payload: newTask.trim() });
      setNewTask("");
    }
  };

  const removeTask = (index) => {
    dispatch({ type: "REMOVE_TASK", payload: index });
  };

  return (
    <View style={styles.container}>
      <View style={styles.inputContainer}>
        <TextInput
          style={styles.input}
          value={newTask}
          onChangeText={setNewTask}
          placeholder="Lisää uusi tehtävä"
        />
        <Button title="Tallenna" onPress={addTask} />
      </View>
      <FlatList
        data={state.tasks}
        renderItem={({ item, index }) => (
          <TodoItem task={item} onPress={() => removeTask(index)} />
        )}
        keyExtractor={(_, index) => index.toString()}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  inputContainer: {
    flexDirection: "row",
    marginBottom: 20,
  },
  input: {
    flex: 1,
    marginRight: 10,
    borderWidth: 1,
    borderColor: "#ccc",
    padding: 10,
  },
});
