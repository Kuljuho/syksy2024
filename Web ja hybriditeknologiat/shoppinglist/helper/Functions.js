import { firestore } from "../firebase/Config";

export const convertFirebaseTimeStampToJS = (timestamp) => {
  if (!timestamp) {
    return "";
  }
  const firebaseTime = new Date(
    timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000
  );
  const result =
    firebaseTime.getDate() +
    "." +
    (firebaseTime.getMonth() + 1) +
    "." +
    firebaseTime.getFullYear() +
    " " +
    firebaseTime.getHours() +
    "." +
    String(firebaseTime.getMinutes()).padStart(2, "0") +
    "." +
    String(firebaseTime.getSeconds()).padStart(2, "0");
  return result;
};
