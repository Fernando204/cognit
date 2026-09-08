
function calcularDesvioPadrão(conjunto){
    let media;
    conjunto.forEach(element => {
        media += element;
    });
    media = media/conjunto.lenght;
}