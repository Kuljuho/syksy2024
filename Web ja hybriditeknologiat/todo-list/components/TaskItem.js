import React from 'react';
import { TouchableOpacity, Text, StyleSheet } from 'react-native';

const TaskItem = ({ task, onToggle }) => (
  <TouchableOpacity style={styles.task} onPress={onToggle}>
    <Text style={[styles.taskText, task.done && styles.taskDone]}>
      {task.text}
    </Text>
  </TouchableOpacity>
);

const styles = StyleSheet.create({
  task: {
    padding: 15,
    borderBottomWidth: 1,
    borderColor: '#ccc',
  },
  taskText: {
    fontSize: 16,
  },
  taskDone: {
    textDecorationLine: 'line-through',
    color: '#888',
  },
});

export default TaskItem;