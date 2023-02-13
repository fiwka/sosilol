document.querySelector('textarea').addEventListener('keydown', function (e) {
    if (e.key == 'Tab') {
        e.preventDefault();
        const start = this.selectionStart;
        const end = this.selectionEnd;

        this.value = this.value.substring(0, start) +
            "\t" + this.value.substring(end);

        this.selectionStart =
            this.selectionEnd = start + 1;
    }
});

document.addEventListener('keydown', async e => {
    if (e.ctrlKey && e.code === 'KeyS') {
        e.preventDefault();
        await save()
    }
})

// const textarea = document.querySelector('textarea')
// const lineNumbers = document.querySelector('.line-numbers')
//
// textarea.addEventListener('keyup', event => {
//     const numberOfLines = event.target.value.split('\n').length
//
//     lineNumbers.innerHTML = Array(numberOfLines)
//         .fill('<span></span>')
//         .join('')
// })

async function save() {
    console.log('a')
    const textArea = document.querySelector('textarea')
    const text = textArea.value

    if (!text || text.length < 1)
        return

    const detected = hljs.highlightAuto(text).value;
    textArea.remove()
    document.querySelector('#save-btn').remove()
    const div = document.createElement('div')
    div.classList.add("code-view")
    div.classList.add("default-section")
    div.innerHTML = detected
    document.body.appendChild(div)
    const httpResult = await fetch("/save", { method: "POST", body: text })
    const result = await httpResult.text();
    history.pushState(null, null, `/view/${result}`)
}

document.querySelector('#save-btn').addEventListener('click', _ => save())