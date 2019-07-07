'use strict';
let gulp = require("gulp");
let uglify = require("gulp-uglify");
let prefixer = require('gulp-autoprefixer');
let cssmin = require('gulp-clean-css');
let clean = require('gulp-clean');
let inject = require('gulp-inject');
let concat = require('gulp-concat');
let nodePath = require('path');
let fs = require('fs');
let concatCss = require('gulp-concat-css');
let order = require("gulp-order");

var protractor = require("gulp-protractor").protractor;
var webdriver_standalone = require("gulp-protractor").webdriver_standalone;
var webdriver_update = require("gulp-protractor").webdriver_update;
//-----------------------------------------------------

let path = {
    rootPath: 'src/main/resources/static/',
    build: {
        index: 'build/resources/main/static/',
        html: 'build/resources/main/static/partials/',
        js: 'build/resources/main/static/scripts/',
        css: 'build/resources/main/static/assets/css/',
        img: 'build/resources/main/static/assets/img/',
        fonts: 'build/resources/main/static/assets/fonts/',
        vender: 'build/resources/main/static/assets/vender/'
    },
    src: {
        html: 'partials/**/**/**/*.html',
        js: 'scripts/**/',
        js_m: 'scripts/**/*.js',
        css: 'assets/css/**/*.css',
        img: 'assets/img/**/*.*',
        fonts: 'assets/vender/fonts/**/*.*',
        vender: 'assets/vender/**/*.*'
    },
    watch: {
        html: 'partials/modules/**/**/**/*.html',
        js: 'scripts/modules/**/**/**/**/**/**/*.js',
        css: 'assets/css/*.css',
        img: 'assets/img/*.*',
        fonts: 'assets/fonts/*.*'
    },
    clean: './build'
};

let parseLinks = (filePath) => {
    let content = fs.readFileSync(filePath, 'utf8');

    let arRe = [{
            type: 'css',
            reTag: /<!--.source:css.-->([.\s\S]*?)<!--.endinject.-->/gi,
            reLink: /href.*=.*['"](.*)['"].*/gi
        },
        {
            type: 'js',
            reTag: /<!--.source:js.-->([.\s\S]*?)<!--.endinject.-->/gi,
            reLink: /src.*=.*['"](.*)['"].*/gi
        }
    ];
    let result = {};
    arRe.map((re) => {
        if (!result[re.type])
            result[re.type] = []
        let strTags = re.reTag.exec(content);
        if (strTags && strTags[1]) {
            let strLinks;
            while ((strLinks = re.reLink.exec(strTags[1]))) {
                if (strLinks) {
                    result[re.type].push(nodePath.join(path.rootPath, './', strLinks[1]));
                }
            }
        }
    });
    return result;
};

//-----------------------------------------------------
// BUILD TASKS
//-----------------------------------------------------
gulp.task('html-build', ['cleanBuild'], function() {
    gulp.src(nodePath.join(path.rootPath, path.src.html))
        .pipe(gulp.dest(path.build.html));
});
//-----------------------------------------------------
gulp.task('assets-build', ['html-build'], function() {
    gulp.src(['src/main/resources/application.properties', 'src/main/resources/log4j2.xml', 
              'src/main/resources/bootstrap.yml'])
        .pipe(gulp.dest('build/resources/main/'));

    gulp.src(nodePath.join(path.rootPath, path.src.vender))
        .pipe(gulp.dest(path.build.vender));

    // gulp.src(nodePath.join(path.rootPath, path.src.css))
    //     .pipe(cssmin())
    //     .pipe(gulp.dest(path.build.css));

    // for font-awesome
    let fontAwesomeFonts = [ // TODO check it
        nodePath.join(path.rootPath, 'node_modules/font-awesome/fonts/fontawesome-webfont.woff'),
        nodePath.join(path.rootPath, 'node_modules/font-awesome/fonts/fontawesome-webfont.woff2'),
        nodePath.join(path.rootPath, 'node_modules/font-awesome/fonts/fontawesome-webfont.ttf')
    ];

    gulp.src(fontAwesomeFonts.concat(nodePath.join(path.rootPath, path.src.fonts)))
        .pipe(gulp.dest(path.build.fonts));

    gulp.src(nodePath.join(path.rootPath, path.src.img))
        .pipe(gulp.dest(path.build.img));
});
//-----------------------------------------------------
gulp.task('js-tpl-build', ['cleanBuild'], function() {
    return gulp.src([nodePath.join(path.rootPath, path.src.js + '*.html')])
        .pipe(gulp.dest(path.build.js));
});
//-----------------------------------------------------
gulp.task('cleanBuild', function() {
    return gulp.src(['./build/resources/main/'])
        .pipe(clean());
});
//-----------------------------------------------------
gulp.task('build', ['html-build', 'assets-build', 'js-tpl-build'], function() {
    let target = gulp.src(nodePath.join(path.rootPath, 'index.html'))
        .pipe(gulp.dest('./build/resources/main/static/'));

    let arSources = parseLinks(nodePath.join(path.rootPath, 'index.html'));
    // To control CSS and JS files' concat order just uncomment the line below
    // console.log(arSources);
    let sourcesJs = gulp.src(arSources.js)
        .pipe(concat('app.min.js'))
        .pipe(uglify()) // if you want to use unsafe injects try "uglify({ mangle: false })"
        .pipe(gulp.dest(nodePath.join(path.build.js)));

    let sourcesCss = gulp.src(arSources.css)
        .pipe(concatCss('main.min.css'))
        .pipe(prefixer())
        .pipe(cssmin())
        .pipe(gulp.dest(nodePath.join(path.build.css)));

    target
        .pipe(inject(sourcesCss, { name: 'source', relative: true }))
        .pipe(sourcesJs ? inject(sourcesJs, { name: 'source', relative: true }) : '')
        .pipe(gulp.dest(path.build.index));

    // support for kiosk mode
    let kioskTarget = gulp.src(nodePath.join(path.rootPath, 'kiosk.html'))
        .pipe(gulp.dest('./build/resources/main/static/'));

    let kioskSources = parseLinks(nodePath.join(path.rootPath, 'kiosk.html'));

    let kioskJsSources = gulp.src(kioskSources.js)
        .pipe(concat('kiosk.min.js'))
        .pipe(uglify()) // if you want to use unsafe injects try "uglify({ mangle: false })"
        .pipe(gulp.dest(nodePath.join(path.build.js)));

    let kioskCssSources = gulp.src(kioskSources.css)
        .pipe(concatCss('kiosk.min.css'))
        .pipe(prefixer())
        .pipe(cssmin())
        .pipe(gulp.dest(nodePath.join(path.build.css)));

    kioskTarget
        .pipe(inject(kioskCssSources, { name: 'source', relative: true }))
        .pipe(kioskJsSources ? inject(kioskJsSources, { name: 'source', relative: true }) : '')
        .pipe(gulp.dest(path.build.index));
});
//-----------------------------------------------------
gulp.task('build_dev', ['html-build', 'assets-build', 'js-tpl-build'], function() {
    let target = gulp.src(nodePath.join(path.rootPath, 'index.html'))
        .pipe(gulp.dest('./build/resources/main/static/'));

    let arSources = parseLinks(nodePath.join(path.rootPath, 'index.html'));

    let sourcesJs = gulp.src(arSources.js)
        .pipe(gulp.dest(nodePath.join(path.build.js)));

    let sourcesCss = gulp.src(arSources.css)
        .pipe(gulp.dest(nodePath.join(path.build.css)));

    target
        .pipe(inject(sourcesCss, { name: 'source', relative: true }))
        .pipe(sourcesJs ? inject(sourcesJs, { name: 'source', relative: true }) : '')
        .pipe(gulp.dest(path.build.index));
});


//-----------------------------------------------------
// E2E TESTS
//-----------------------------------------------------
gulp.task('webdriver_standalone', webdriver_standalone);

gulp.task('webdriver_update', webdriver_update);

/// Returns cli parameter value (e.g. gulp task --param value)
function getClParam(name) {
    var argIndex = !!process.argv ? process.argv.indexOf('--' + name) : -1;
    return argIndex > -1 ? process.argv[argIndex + 1] : undefined;
}

// NOTE: before run the tests run "gulp webdriver_update" for download selenium webdriver
// NOTE: for run custom folder test type: gulp e2e --folder devices
gulp.task('e2e', function() {

    // get folder form cl args
    var folderForTest = getClParam('folder');

    var specForTest = folderForTest ? ["./e2e/common/*.js", "./e2e/pages/login/*.js", "./e2e/pages/" + folderForTest + "/*.js"] : ["./e2e/**/*.js"];
    var specOrdering = folderForTest ? ["login.spec.js"] : ["pages/registration/*.js", "pages/login/*.js"];

    gulp.src(specForTest)
        .pipe(order(specOrdering))
        .pipe(protractor({
            configFile: "protractor.config.js",
            args: ['--baseUrl', 'http://127.0.0.1:12003']
        }))
        .on('error', function(e) { throw e; });
});