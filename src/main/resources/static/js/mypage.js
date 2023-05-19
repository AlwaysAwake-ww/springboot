






$('#infoUpdateBtn').on('click', function(){

    console.log('infoUpdateBtn click');
    $('#introduction').prop('readonly', false);
    $('#infoUpdateSubmitBtn').css('display', 'inline-block');
    $('#updateBefore').css('display', 'inline-block');
    $('#updateSubmit').css('display', 'none');
    $(this).css('display', 'none');

});




$('#infoUpdateSubmitBtn').on('click', function(){

    console.log('infoUpdateSubmitBtn click');
    // ajax로 수정된 데이터 전송, 저장
    introductionUpdate($(this));

});


function introductionUpdate(object){

    
    $.ajax({
        url: '/user/mypage/update',
        async: true,
        data: {
            introduction: $('#introduction').val()
        },
        method: 'POST'
    })
        .done(function (text) {
            
            $('#introduction').text(text);
            $('#introduction').prop('readonly', true);
            $('#infoUpdateBtn').css('display','inline-block');
            $('#updateBefore').css('display', 'none');
            $('#updateSubmit').css('display', 'inline-block');
            object.css('display', 'none');
        })
        .fail(function (text) {
            alert('ajax fail');
        });

} 