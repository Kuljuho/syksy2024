import React from 'react';
import { Appbar } from 'react-native-paper';

const CustomAppBar = ({ navigation, back, options }) => {
  return (
    <Appbar.Header>
      {back ? (
        <Appbar.BackAction onPress={navigation.goBack} />
      ) : (
        <Appbar.Action icon="arrow-right" onPress={() => navigation.navigate('Second')} />
      )}
      <Appbar.Content title={options.title} />
    </Appbar.Header>
  );
};

export default CustomAppBar;