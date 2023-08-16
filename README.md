# Rating Reader
#### By Sam Salek

Rating Reader is a Windows application that reads media scores along with other useful information from [**IMDb**](https://www.imdb.com) and conveniently displays them for you in a spoiler-free way.

The application works by parsing the HTML from IMDb's website with the library [**jsoup**](https://github.com/jhy/jsoup). 
The IMDb website is constantly changing - 
this means Rating Reader will eventually stop working when it won't be able to read the site's new HTML layout.
- Most recently tested to be working at: **2022-03-26** (Rating Reader may not work after this date).

#### Note:
* Needs an internet connection to work (obviously).
* Developed, tested, and working on Windows 10. Functionality for other platforms or Windows versions is unknown.

## How to download
You can run the application either through the runnable .jar or .exe. 
The most recent version of both files can be downloaded from the [**releases page**](https://github.com/sam-salek/RatingReader/releases/).
- **.jar:** A recent JRE needs to be installed to run the .jar.
- **.exe:** For the .exe to be able to run, it **must always** be located in the same directory as the included "win32" folder. A shortcut of the .exe can be created and placed wherever for easier access. 


