@import ru.kdev.sosilol.data.Paste

@param Paste paste

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat&display=swap" rel="stylesheet">
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/highlight.js/11.5.1/styles/atom-one-dark.min.css">
    <script src="//cdnjs.cloudflare.com/ajax/libs/highlight.js/11.5.1/highlight.min.js"></script>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script>hljs.highlightAll();</script>
    <link rel="stylesheet" href="/common.css">
    <title>соси лол</title>
</head>

<body>
    <nav>
        <div class="nav-wrapper" style="background-color: #1e1e1e;">
            <a href="/" class="brand-logo" style="margin-left: 10px;">соси лол</a>
            <a href="#" data-target="mobile" class="sidenav-trigger"><i class="material-icons">menu</i></a>
            <ul id="nav-mobile" class="right hide-on-med-and-down">
                <li><a href="/v1/api-docs-ui">API</a></li>
                <li><a href="https://vk.com/fiwka">Автор</a></li>
                <li><a href="/profile">Профиль</a></li>
            </ul>
        </div>
    </nav>
    <ul class="sidenav" id="mobile">
        <li><a href="/v1/api-docs-ui">API</a></li>
        <li><a href="https://vk.com/fiwka">Автор</a></li>
        <li><a href="/profile">Профиль</a></li>
    </ul>
    <div class="default-section code-view">${paste.getCode()}</div>
    <script>
        const codeview = document.querySelector(".code-view");
        codeview.innerHTML = hljs.highlightAuto(codeview.innerText).value
    </script>
    <script src="/menu.js"></script>
</body>

</html>