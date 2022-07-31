pushd ..\src\main
javac.exe LifoSort.java
jar cvf ..\..\lifosort.jar *.class layout\*.class
call noclass
popd
