javascript: (
    function() {

        var css = "";

        var issuesNavBarName = "reponav-wrapper reponav-small d-lg-none";
        var gitHubNavBarName = "position-relative js-header-wrapper ";
		var projectButton = "d-flex d-sm-none position-fixed bottom-0 width-full border-top bg-white p-3 box-shadow-large js-project-small-footer";

        var head = document.getElementsByTagName("head")[0];
        var body = document.getElementsByTagName("body")[0];
        var style = document.createElement("style");

        style.type = "text/css";
        if (style.styleSheet) {
            style.styleSheet.cssText = css;
        } else {
            style.appendChild(document.createTextNode(css));
        }

        if (document.getElementsByClassName(gitHubNavBarName)[0]) {
            document.getElementsByClassName(gitHubNavBarName)[0]
                    .style
                    .display = "none";
        }
        if (document.getElementsByClassName(issuesNavBarName)[0]) {
            document.getElementsByClassName(issuesNavBarName)[0]
                    .style
                    .display = "none";
        }
        if (document.getElementsByClassName(projectButton)[0]) {
            document.getElementsByClassName(projectButton)[0]
                    .style
                    .marginBottom = "55px";
            document.body.style.marginBottom="150px";
        } else {
            document.body.style.marginBottom="55px";
        }

        head.appendChild(style);
        body.appendChild(style);

    }());