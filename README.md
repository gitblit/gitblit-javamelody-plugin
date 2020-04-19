## Gitblit JavaMelody plugin

*REQUIRES 1.6.0*

The Gitblit [JavaMelody](https://github.com/javamelody/javamelody/wiki) plugin provides realtime monitoring of your Gitblit installation by JavaMelody.

Only *administrators* may access the JavaMelody app.

### Installation

This plugin is referenced in the Gitblit Plugin Registry and you may install it using SSH with an administrator account.

    ssh host plugin refresh
    ssh host plugin install javamelody
    ssh host plugin ls

Alternatively, you can download the zip and manually copy it to your `${baseFolder}/plugins` directory.

### Setup

There is no initial setup for this plugin. However, if there are JavaMelody [settings](https://github.com/javamelody/javamelody/wiki/UserGuide#6-optional-parameters) that you want to configure, you may specify them in your `gitblit.properties` file using the *javamelody.* prefix.

e.g.

    javamelody.monitoring-path = /jm
    javamelody.storage-directory = /tmp/javamelody

### Usage

Once the plugin is installed *administrators* may access it via the user menu in the upper-right corner.  There will be a menu item link for the JavaMelody web ui.

### Authors

- [David Ostrovsky](https://github.com/davido) (original author)
- [gitblit.com](https://github.com/gitblit) (maintainer)

### Building against a Gitblit RELEASE

    ant && cp build/target/javamelody*.zip /path/to/gitblit/plugins

### Building against a Gitblit SNAPSHOT

    /path/to/dev/gitblit/ant installMoxie
    /path/to/dev/javamelody/ant && cp build/target/javamelody*.zip /path/to/gitblit/plugins

