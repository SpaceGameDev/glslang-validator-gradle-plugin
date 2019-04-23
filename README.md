# glslang-validator-gradle-plugin

This gradle plugin uses glslangValidator to compile your glsl files (.vert, .frag etc.) into SPIR-V binaries (.spv) to be used in your vulkan game. 
It only builds when you make changes to your files but only does full builds (no incremental).
By default it uses the sourcesSets name glsl and the glsl folder for compiling (will be next to your java or resources folder).



# Usage: 

Add this to the top of your build.gradle: 
```groovy
plugin {
	id: 'glslang-validator-gradle-plugin'
}
```
You probably already have other plugins like `java-library` or `application`


It won't be able to find the plugin as it is not contained in the main gradle plugin repos.
So currently the easiest way will be to do the steps described in `Development of the Plugin`.



# Development of the Plugin:
 
To develop this plugin you want to be able to edit the source while also having your own project open to see the changes you do in effect.

1. Add this repo as a subproject to your git repo:

`bash:`
```bash
$ git submodule add https://github.com/SpaceGameDev/glslang-validator-gradle-plugin.git glslang-validator-gradle-plugin
```

2. clone and/or pull the subproject:

`bash:`
```bash
$ git submodule update
```

3. Tell gradle to include this gradle project (or build in gradle terms) into your project (or build).

`settings.gradle:` 
```groovy
includeBuild 'glslang-validator-gradle-plugin'
```
As composite builds of plugins can sometimes be funky it is recommended to put this line **before** any other includeBuild and in all root projects

Now gradle will instead of downloading it as a library use the project directly and build from source
