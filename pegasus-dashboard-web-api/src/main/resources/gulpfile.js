'use strict';

var del = require('del');
var gulp = require('gulp');
var inject = require('gulp-inject');
var concat = require('gulp-concat');
var concatCss = require('gulp-concat-css');
var angularFilesort = require('gulp-angular-filesort');
var templateCache = require('gulp-angular-templatecache');
var htmlmin = require('gulp-htmlmin');
var sass = require('gulp-sass');

gulp.task('clean', function(done) {
    del(['./dist', './.tmp'], done);
});

gulp.task('build', ['concatJS', 'concatCss'], function() {});

gulp.task('concatJS', ['concatHtml'], function() {
    return gulp
        .src(['./src/**/*.js', './.tmp/templates.module.temp.js'])
        .pipe(angularFilesort())
        .pipe(concat('dynamic-dashboard.module.js'))
        .pipe(gulp.dest('./dist/'));
});

gulp.task('concatHtml', function() {
    return gulp
        .src('./src/**/*.html')
        .pipe(htmlmin({ collapseWhitespace: true }))
        .pipe(templateCache({ module: 'widgets', root: '/views', filename: 'templates.module.temp.js' }))
        .pipe(gulp.dest('.tmp'));
});

gulp.task('concatCss', function() {
    return gulp
        .src('./src/**/*.scss')
        .pipe(sass().on('error', sass.logError))
        .pipe(concatCss('dynamic-dashboard.module.css'))
        .pipe(gulp.dest('./dist/'));
});

gulp.task('default', function() {
    gulp.start('build');
});
