export function getOrCreate<T>(cache: Map<string, T>, key: string, factory: () => T): T {
  const cached = cache.get(key);
  if (cached !== undefined) {
    return cached;
  }

  const value = factory();
  cache.set(key, value);
  return value;
}

export function seedFromKey(key: string): number {
  let hash = 0;
  for (let i = 0; i < key.length; i++) {
    hash = (hash << 5) - hash + key.charCodeAt(i);
    hash |= 0;
  }
  return Math.abs(hash);
}
