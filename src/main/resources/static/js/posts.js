

window.onload =
function replaceTumbnail(){
    const imgs = document.getElementsByTagName('img');
    const postIndex =  document.getElementsByName('postIndex');

    for (var i = 0; i < imgs.length; i++) {

        const selectImg = imgs[i];
        const img = new Image();

        img.src = '/uploadresult/images/thumbnail/' + postIndex[i].textContent;
        selectImg.src = img.src;

    }


};



$('#searchPostBtn').on('click', function(){

    $.get('/user/allpost', function(data){

        console.log(data);
    })

});