import { initializeApp } from "firebase/app";
import {
  getFirestore,
  collection,
  addDoc,
  deleteDoc,
  doc,
  onSnapshot,
  query,
  orderBy,
} from "firebase/firestore";
import { getAuth, signInWithEmailAndPassword } from "firebase/auth";
import { firebaseConfig } from './ConfigSecrets';

initializeApp(firebaseConfig);

const firestore = getFirestore();
const auth = getAuth();

const SHOPPING_ITEMS = "shoppingItems";

export {
  firestore,
  collection,
  addDoc,
  deleteDoc,
  doc,
  onSnapshot,
  query,
  orderBy,
  SHOPPING_ITEMS,
  signInWithEmailAndPassword,
  auth,
  getAuth,
};
