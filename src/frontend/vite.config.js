import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  // Your Vite config options
  build: {
    outDir: 'dist',  // output directory
  },base: '/court/', 
  // server: {
  //   proxy: {
  //     // Proxy all requests to the target server
  //     '/court': {
  //       target: 'http://localhost:8080', // The backend server to proxy to
  //       changeOrigin: true,
  //       secure: false,  // If you're using a self-signed SSL certificate
  //     },
  //   },
  // },
})
