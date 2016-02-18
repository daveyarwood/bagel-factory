# bagel-factory

This is my experiment: writing a bookmarklet in ClojureScript, along with a Boot task that will spit out an HTML file containing the bookmarklet link that will run the JavaScript.

**UNDER CONSTRUCTION**

~~Step 1 is to get the Boot task pipeline working -- once I've got that, I will break it out into a standalone Boot task.~~ done

Step 2 is to use the Boot task to write a bookmarklet (in ClojureScript) that replaces programming terms with breakfast terms.

## Status / TODO

* I have a working bookmarklet that does text replacement on the whole page, matching case. Unfortunately the bookmarklet link URL is 138,581 characters long, well over the 2000 character limit for a URL!

* Still need to add a bunch of programming/breakfast word pairings, e.g. public => potato, static => sausage, void => blintz, etc.

* At least in Chrome, I am still able to click the link on the HTML page that boot-bookmarklet generates, and it appears to be working. However, I can't drag the link into my bookmarks bar, presumably because the URL is way too long.

* Next steps:

  * Host the js file somewhere like on S3.

    * Maybe use a Boot task to automate the process of building and uploading the js file.

  * Generate a bookmarklet that just sources the hosted js file.

    * Consider building this functionality into boot-bookmarklet since it will be super common.
