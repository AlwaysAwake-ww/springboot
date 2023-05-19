
const searchForm = document.getElementById('searchForm');
const searchInput = document.getElementById('searchInput');
const searchCategory = document.getElementById('searchCategory');
const searchBtn = document.getElementById('searchBtn');



const searchList = document.getElementById('searchList');
const searchListBody = document.getElementById('searchListBody');


const selectMemberName = document.getElementById('selectMemberName');
const selectMemberEmail = document.getElementById('selectMemberEmail');
const selectMemberIntroduction = document.getElementById('selectMemberIntroduction');
const selectMemberJoinDate = document.getElementById('selectMemberJoinDate');

const searchValid = document.getElementById('searchValid');

const memberDetail = document.getElementById('memberDetail');
const addTeamBtn = document.getElementById('addTeamBtn');

const teamMemberList = document.getElementById('teamMemberList');
const teamMemberListBody = document.getElementById('teamMemberListBody');

const memberListValid = document.getElementById('memberListValid');
const teamName = document.getElementById('teamName');

searchBtn.addEventListener('click', function () {


    if (searchInput.value == "") {
        searchValid.style = "";
    }
    else {
        searchValid.style = "display:none";
        $.ajax({
            url: '/user/search',
            method: 'post',
            async: true,
            data: {
                searchCategory: searchCategory.value,
                searchKeyword: searchInput.value
            },
            dataType: 'json'

        })
            .done(function (data) {

                console.log('ajax success :: ');

                addSearchList(data);

            })
            .fail(function (data) {


                console.log('ajax fail :: ' + data);

            })
    }


});


searchCategory.addEventListener('change', function () {

    if (searchCategory.value == 'searchEmail') {

        searchInput.type = 'email';
    }
    else {
        searchInput.type = 'text';
    }

});



function addSearchList(data) {

    searchListBody.innerHTML = "";

    data.forEach(function (e) {

        const row = document.createElement('tr');
        const memberName = document.createElement('td');
        const memberEmail = document.createElement('td');

        memberName.innerHTML = e.memberName;
        memberEmail.innerHTML = e.memberEmail;

        row.appendChild(memberName);
        row.appendChild(memberEmail);

        row.addEventListener('click', function () {


            const email = this.cells[1].innerHTML;


            ajaxMemberDetail(email);
            memberDetail.style = "";

        })

        searchListBody.appendChild(row);

    });
};


function ajaxMemberDetail(email) {


    $.ajax({
        url: '/user/search',
        async: true,
        data: {
            searchCategory: 'searchEmail',
            searchKeyword: email
        },
        method: 'POST'
    })
        .done(function (data) {

            memberDomain = data[0];


            selectMemberName.innerHTML = memberDomain.memberName;
            selectMemberEmail.innerHTML = memberDomain.memberEmail;
            selectMemberIntroduction.innerHTML = memberDomain.memberIntroduction;
            selectMemberJoinDate.innerHTML = memberDomain.joinDate;


        })



}


addTeamBtn.addEventListener('click', function () {


    addTeamMemberList();

});


function addTeamMemberList() {

    const row = document.createElement('tr');
    const memberName = document.createElement('td');
    const memberEmail = document.createElement('td');
    const memberJoinDate = document.createElement('td');
    const deleteLine = document.createElement('td');

    const memberDeleteBtn = document.createElement('input');

    memberName.innerHTML = selectMemberName.innerHTML;
    memberEmail.innerHTML = selectMemberEmail.innerHTML;
    memberJoinDate.innerHTML = selectMemberJoinDate.innerHTML;


    memberDeleteBtn.type = 'button';
    memberDeleteBtn.classList.add('btn');
    memberDeleteBtn.classList.add('btn-danger');
    memberDeleteBtn.value = '-';

    memberDeleteBtn.addEventListener('click', function(){
        this.closest('tr').remove();
    });
    
    deleteLine.appendChild(memberDeleteBtn);

    row.appendChild(memberName);
    row.appendChild(memberEmail);
    row.appendChild(memberJoinDate);
    row.appendChild(deleteLine);
    teamMemberListBody.appendChild(row);

}


teamListBtn.addEventListener('click', function(){

    const rows = teamMemberList.rows;

    const memberEmailArray = new Array();
    

    if(rows.length>1){
        
        memberListValid.style="display:none";

        for (var i = 1; i < rows.length; i++) {
            const row = rows[i];
            memberEmailArray.push(row.cells[1].innerHTML);
        }
    
        $.ajax({
            url: '/user/team/create/submit',
            async: true,
            data: {
                teamName:teamName.value,
                memberEmailArray:memberEmailArray || null,
            },
            method: 'POST'
        })
            .done(function (text) {
                
                if(text='success'){
                    window.location='/user/team';
                }
            })
            .fail(function (text) {
                alert('ajax fail');
            });
    
    }

    else{
        
        memberListValid.style="";
    }
    

});