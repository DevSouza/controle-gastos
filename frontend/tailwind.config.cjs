module.exports = {
  content: ["./src/**/*.{html,js,ts,tsx}"],
  theme: {
    fontFamily: {
      'sans': ['Saira'],
      'serif': ['Saira'],
      'mono': ['Saira'],
      'display': ['Saira'],
      'body': ['Saira'],
    },
    extend: {
      gridTemplateColumns: {
        // Simple 16 column grid
        '13': 'repeat(13, 1fr)',
      },
      spacing: {
        'h-util-template': 'calc(100vh - 12.25rem)'
      }
    },
  },
  plugins: [],
}