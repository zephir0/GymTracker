window.onload = function () {

    window.ui = SwaggerUIBundle({
        url: "/GymTracker/src/main/resources/swagger.yml",
        dom_id: '#swagger-ui',
        deepLinking: true,
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIStandalonePreset
        ],
        plugins: [
            SwaggerUIBundle.plugins.DownloadUrl
        ],
        layout: "StandaloneLayout"
    });

};
