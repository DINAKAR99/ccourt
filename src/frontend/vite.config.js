import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  // Your Vite config options
  build: {
    outDir: 'dist',  // output directory
  },base: '/court/', 
})
