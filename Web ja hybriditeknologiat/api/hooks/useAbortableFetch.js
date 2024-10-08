import React from 'react'
import { useState, useEffect, useRef } from 'react'

const useAbortableFetch = (url) => {
  const [data, setData] = useState(null)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)
  const controllerRef = useRef()

  useEffect(() => {
    getData(url)
  }, [url])

  const getData = (url) => {
    if (controllerRef.current) {
      controllerRef.current.abort()
    }
    controllerRef.current = new AbortController()
    const signal = controllerRef.current.signal

    setLoading(true)
    fetch(url, { signal })
      .then(response => response.json())
      .then(json => setData(json))
      .catch(error => setError(error))
      .finally(() => setLoading(false))
  }

  return { data, error, loading }
}

export default useAbortableFetch
