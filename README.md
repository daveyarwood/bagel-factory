# bagel-factory

This is my experiment: writing a bookmarklet in ClojureScript, along with a Boot task that will spit out an HTML file containing the bookmarklet link that will run the JavaScript.

**UNDER CONSTRUCTION**

~~Step 1 is to get the Boot task pipeline working -- once I've got that, I will break it out into a standalone Boot task.~~ [done! boot-bookmarklet is a thing now](https://github.com/adzerk-oss/boot-bookmarklet)

Step 2 is to use boot-bookmarklet to write a bookmarklet (in ClojureScript) that replaces programming terms with breakfast terms.

## Status / TODO

* I have a working bookmarklet that replaces a couple words on the current page when you click it.

* Still need to add a bunch of programming/breakfast word pairings, e.g. public => potato, static => sausage, void => blintz, etc.

* `external-bookmarklet` Boot task generates an html file with a bookmarklet link that sources a hosted .js file.

* For the moment, I've compiled the ClojureScript source into JavaScript via `boot cljs` and manually uploaded the resulting `bagel_factory.js` file to Dropbox with a public URL, which I then fed to the `external-bookmarklet` task to generate the bookmarklet.

* The next step is to automate this process into a single build task.

