@import ru.kdev.sosilol.service.GithubProfile

@param GithubProfile profile

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="/common.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat&display=swap" rel="stylesheet">
    <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
    <title>Профиль</title>
</head>
<body>
    <nav>
        <div class="nav-wrapper" style="background-color: #1e1e1e;">
            <a href="/" class="brand-logo" style="margin-left: 10px;">соси лол</a>
            <ul id="nav-mobile" class="right hide-on-med-and-down">
                <li><a href="/v1/api-docs-ui">API</a></li>
                <li><a href="https://vk.com/fiwka">Автор</a></li>
                <li><a href="/profile">Профиль</a></li>
            </ul>
        </div>
    </nav>
    <section class="default-section" id="profile">
        <header style="text-align: center; user-select: none;">
            <img src="${profile.getAvatarUrl()}" alt="Avatar" class="avatar">
            <h3>Профиль ${profile.getLogin()}</h3>
        </header>
        <div class="pastes" style="text-align: center; user-select: none;">
            @if(profile.getPastes().isEmpty())
            <h4 style="color: #676a6d">У вас нет сохранённых паст.</h4>
            @else
            <h4 style="color: #676a6d">Ваши пасты:</h4>
            <div class="collection" style="outline: none !important; border: none !important;">
                @for(var element : profile.getPastes())
                <a href="/view/${element.getId()}" class="collection-item grey darken-3">${element.getId()}</a>
                @endfor
            </div>
            @endif
        </div>
    </section>
</body>
</html>