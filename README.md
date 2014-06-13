## Gitblit JavaMelody plugin

*REQUIRES 1.6.0*

The Gitblit [JavaMelody](https://code.google.com/p/javamelody) plugin provides realtime monitoring of your Gitblit installation by JavaMelody.

Only *administrators* may access the JavaMelody app.

### Installation

This plugin is referenced in the Gitblit Plugin Registry and you may install it using SSH with an administrator account.

    ssh host plugin refresh
    ssh host plugin install javamelody
    ssh host plugin ls

Alternatively, you can download the zip and manually copy it to your `${baseFolder}/plugins` directory.

### Setup

There is no initial setup for this plugin.  However, if there are JavaMelody settings that you want to configure, you may specify them in your `gitblit.properties` file.



### Building against a Gitblit RELEASE

    ant && cp build/target/javamelody*.zip /path/to/gitblit/plugins

### Building against a Gitblit SNAPSHOT

    /path/to/dev/gitblit/ant installMoxie
    /path/to/dev/javamelody/ant && cp build/target/javamelody*.zip /path/to/gitblit/plugins

