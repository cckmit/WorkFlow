//Dependencies
var gulp = require('gulp');
var browserSync = require('browser-sync').create();
var useref = require('gulp-useref');
var uglify = require('gulp-uglify');
var gulpIf = require('gulp-if');
var imagemin = require('gulp-imagemin');
var del = require('del');
var cleanCSS = require('gulp-clean-css');
var cache = require('gulp-cache');
var runSequence = require('run-sequence');
var gulpSequence = require('gulp-sequence')
var environment = require("./conf.json")
var replace = require('gulp-replace');

//Set REST API path
gulp.task('set-env', function(){
  if(process.argv.length !==  5){
    console.log("Provide Environment Information")
    return;
  }
  gulp.src(['app/js/config.js'])
    .pipe(replace(/apiBase.*/g, 'apiBase: '+'"'+environment[process.argv[4]].api+'",'))
    .pipe(gulp.dest('app/js/.'));
});
//Delete the dist folder
gulp.task('clean:dist', function() {
  return del.sync('dist');
})

//Clear system cache
gulp.task('cache:clear', function (callback) {
    return cache.clearAll(callback)
})

//Browser live reload on change of files
gulp.task('browserSync', function() {
  browserSync.init({
    server: {
      baseDir: 'app'
    },
  })
})
//Run dist
gulp.task('run:dist', function() {
  browserSync.init({
    server: {
      baseDir: 'dist'
    },
  })
})
//Task to watch change of files while development
//Reload browser page on change of selected files
gulp.task('serve', ['browserSync'], function(){
    gulp.watch('app/**/*.html', browserSync.reload);
    gulp.watch('app/**/*.js', browserSync.reload);
    gulp.watch('app/**/*.css', browserSync.reload);
})

//Minify the images
gulp.task('minImages', function(){
  return gulp.src('app/images/**/*.+(png|jpg|gif|svg)')
  .pipe(imagemin())
  .pipe(gulp.dest('dist/images'))
});

//Minify the fonts
gulp.task('minFonts', function() {
  return gulp.src('app/fonts/*')
  .pipe(gulp.dest('dist/fonts/.'))
})

//Bundling css and js files
//Minify bundled css and js files
gulp.task('useref', function(){
  return gulp.src('app/*.html')
    .pipe(useref())
//    .pipe(gulpIf('*.js', uglify()))
    .pipe(gulp.dest('dist'))
});

/*gulp.task('moveJs', function(){
  return gulp.src(['app/controllers*//*.js','app/controllers*//*.js'])
    .pipe(gulp.dest('dist'))
});*/
gulp.task('moveCss', function(){
  return gulp.src('app/css/*.css')
    .pipe(gulp.dest('dist/css/'))
});

gulp.task('moveConfig', function(){
  return gulp.src('app/js/config.js')
    .pipe(gulp.dest('dist/js/.'))
});
gulp.task('minify-css', function() {
  return gulp.src('dist/css/*.css')
//    .pipe(cleanCSS({compatibility: 'ie9'}))
    .pipe(gulp.dest('dist/css'));
});
//Move html files
gulp.task('html', function() {
  return gulp.src('app/html/**/*')
  .pipe(gulp.dest('dist/html'))
})
//Build Task
gulp.task('build', function(callback) {
  runSequence('clean:dist','cache:clear','set-env','moveConfig','html','useref', 'moveCss', 'minImages','minFonts', callback);
});
