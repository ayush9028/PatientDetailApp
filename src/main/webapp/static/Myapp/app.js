/*
 * This file launches the application by asking Ext JS to create
 * and launch() the Application class.
 */
Ext.application({
    extend: 'Myapp.Application',

    name: 'Myapp',

    requires: [
        // This will automatically load all classes in the Myapp namespace
        // so that application classes do not need to require each other.
        'Myapp.*'
    ],

    // The name of the initial view to create.
    mainView: 'Myapp.view.main.Main'
});
