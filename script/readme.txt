The files in this 'script' folder are for managing the app from command-line.

Use 'run' with a '-text' option, to get the non-graphic variant.  That one will
run a 'setup' method where the initial tableau is hardcoded, and starts
immediately.  Using a new tableau will require a code change; these are all
embedded in the main and are not read from a file, although there is a file
written when you use the graphic variant.  The contents of that file can be
cut/pasted into a 'setup' method, if it needs further examination and a faster
startup.

The files with a setup<nnn>a.txt name format are the ones used to suss out the
hidden '?' balls.  Copy one, overwrite and save to a new name.  This work is
all done by hand but after getting enough answers you could then enter the 
tableau into the graphical solver.

