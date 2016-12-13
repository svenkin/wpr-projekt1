const resolve = require('path').resolve;

module.exports = {
    entry: './src/main.js',
    output: {
        path: resolve('dist/'),
        publicPath: '/dist/',
        filename: 'bundle.js'
    },
    module: {
        rules: [
            {
                test: /\.vue$/,
                loader: 'vue-loader',
                exclude: 'node_modules/'
            },
            {
               test: /\.js$/,
               exclude: 'node_modules/',
               loader: 'babel-loader',
               query: {
                 presets: ['es2015']
               }
             }
        ]
    },
    resolve: {
        alias: {
            'vue': 'vue/dist/vue.js',
            'vue-router': 'vue-router/dist/vue-router.js',
            'fetch': 'whatwg-fetch/fetch.js'
        }
    },
    devServer: {
        historyApiFallback: true,
        noInfo: true
    }
};
