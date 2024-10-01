import { Appbar } from "react-native-paper";
import React from "react";

export default function MainAppBar(props) {
  return (
    <Appbar.Header style={{ backgroundColor: props.backgroundColor }}>
      {props.back ? (
        <Appbar.BackAction onPress={() => props.navigation.goBack()} />
      ) : null}
      <Appbar.Content title={props.title} />
      {!props.back && (
        <>
          <Appbar.Action icon={props.icon} onPress={props.getUserPosition} />
          <Appbar.Action
            icon="cog"
            onPress={() => props.navigation.navigate("Settings")}
          />
        </>
      )}
    </Appbar.Header>
  );
}
