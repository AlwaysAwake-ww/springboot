
// const str = $("#getcontent").val();
// $("#content").append(str);





window.onload =
    function replaceImgToSrc() {
        const writerIndex = document.getElementById('writerIndex');
        const postIndex = document.getElementById('postIndex');
        const content = document.getElementById('content');
        const imgs = content.getElementsByTagName('img');

        for (var i = 0; i < imgs.length; i++) {

            const selectImg = imgs[i];
            const img = new Image();

            img.src = '/uploadresult/images/' + writerIndex.value + '/' + postIndex.value + '/' + selectImg.id;
            selectImg.src = img.src;



            // const randomValue = Math.random();
            // img.src = `/uploadresult/images/${writerIndex.value}/${selectImg.id}?v=${randomValue}`;
            // selectImg.src = img.src;
        }
    };



$('#deleteBtn').click(function () {
    $('#deleteModal').modal('show');

});

$('#editBtn').click(function () {
    $('#editModal').modal('show');

});


$('#deleteModalBtn').click(function () {

    var url = $(this).data('href');
    $.post(url, function (data) {
        window.location.href = '/user/mypost';
    });

});


$('#editModalBtn').click(function () {

    var url = $(this).data('href');
    window.location.href = url;

});