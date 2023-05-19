



////////////////////////////////////////////////////////////
// 저장, 취소 모달 버튼
///////////////////////////////////////////////////////////

$('#saveBtn').on('click',function(){
    $('#saveModal').modal('show');
});

$('#cancelBtn').on('click',function(){
    $('#cancelModal').modal('show');
});


////////////////////////////////////////////////////////////
// 저장 모달 버튼
///////////////////////////////////////////////////////////





saveModalBtn.addEventListener('click',function () {

    const rows = imageList.rows;
    
    
    const imageOriginNameArr = new Array();
    const imageNewNameArr = new Array();


    
    

    if(rows.length>=1){
        for (i = 1; i < rows.length; i++) {
            imageOriginNameArr.push(rows[i].cells[0].innerHTML);
            imageNewNameArr.push(rows[i].cells[1].innerHTML);
        }
    }

    console.log($('#teamName').val());

    $.ajax({
        url: '/user/write/submit',
        async: true,
        data: {
            title: title.value,
            imageOriginNameArr:imageOriginNameArr || null,
            imageNewNameArr: imageNewNameArr || null,
            content: replaceImgWithId(editor),
            teamName:$('#teamName').val() || null
        },
        method: 'POST'
    })
        .done(function (text) {
            
            if(text=='success'){
                window.location.href='/user/mypost';
            }
            else
                alert(text);
        })
        .fail(function (text) {
            alert('ajax fail');
        });

});





function imgTagRepalce(content) {

    const replaceString = '<img';
    const result = replaceImgTagsWithCount(content, replaceString);

    return result;
};


function replaceImgWithId(editor) {
    const imgs = editor.getElementsByTagName('img');

    for (var i = 0; i < imgs.length; i++) {
        const img = imgs[i];
        const id = img.getAttribute('id');
        if (id) {
            img.outerHTML = '<img id="' + id + '" src="" class="img-fluid">';
        }
    }
    return editor.innerHTML;
};

function replaceImgTagsWithCount(originalString, replacementString) {
    const imgTagPattern = /<img.*?>/g;
    let count = 0;
    const replacedString = originalString.replace(imgTagPattern, function (match) {
        count++;
        return replacementString + count + '>';
    });
    return replacedString;
};
