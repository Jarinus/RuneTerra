var glob = require('glob');
var path = require('path');

module.exports = {
    mode: 'production',
    entry: glob.sync('./src/**/*.js').reduce(function(obj, el){
        var elPath = path.parse(el);
        obj[elPath.dir.replace(/^.*src\/?/, '') + '/' + elPath.name] = el;
        return obj
    },{}),
    output: {
        path: path.resolve(__dirname, 'dist'),
        filename: "[name].js"
    },
    module: {
        rules: [
            { test: /\.js$/, exclude: /node_modules/, loader: "babel-loader" }
        ]
    }
}
