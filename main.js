let users = [];

function saveUsers() {
  localStorage.setItem('skillSharingUsers', JSON.stringify(users));
}

function loadUsers() {
  const data = localStorage.getItem('skillSharingUsers');
  users = data ? JSON.parse(data) : [];
}

function showRegisterPage(pushState = true) {
  document.getElementById('registerPage').classList.add('active');
  document.getElementById('addSkillPage').classList.remove('active');
  document.getElementById('usersPage').classList.remove('active');
  if (pushState) location.hash = "#register";
}

function showAddSkillPage(pushState = true) {
  document.getElementById('registerPage').classList.remove('active');
  document.getElementById('addSkillPage').classList.add('active');
  document.getElementById('usersPage').classList.remove('active');
  displayUsers();
  updateAutocomplete();
  if (pushState) location.hash = "#addskill";
}

function showUsersPage(pushState = true) {
  document.getElementById('registerPage').classList.remove('active');
  document.getElementById('addSkillPage').classList.remove('active');
  document.getElementById('usersPage').classList.add('active');
  displayUsersFull();
  if (pushState) location.hash = "#users";
}

function displayUsers() {
  let html = '';
  users.forEach(user => {
    html += `<div class="user">
      <b>${user.name}</b> (Discord: ${user.discord})<br>
      <strong>Offers:</strong> <ul>${user.skillsOffered.map(s => `<li>${s.name} - ${s.description}</li>`).join('')}</ul>
      <strong>Wants:</strong> ${user.skillsWanted.join(', ')}
    </div>`;
  });
  document.getElementById('users') && (document.getElementById('users').innerHTML = html || "<i>No users registered yet.</i>");
}

function displayUsersFull() {
  let html = '';
  users.forEach(user => {
    html += `<div class="user">
      <b>${user.name}</b> (Discord: ${user.discord})<br>
      <strong>Offers:</strong> <ul>${user.skillsOffered.map(s => `<li>${s.name} - ${s.description}</li>`).join('')}</ul>
      <strong>Wants:</strong> ${user.skillsWanted.join(', ')}
    </div>`;
  });
  document.getElementById('usersFull').innerHTML = html || "<i>No users registered yet.</i>";
}

document.getElementById('registerForm').onsubmit = function(e) {
  e.preventDefault();
  let name = document.getElementById('name').value.trim();
  let discord = document.getElementById('discord').value.trim();
  let skillsOfferedInput = document.getElementById('skillsOffered').value.trim();
  let skillsWantedInput = document.getElementById('skillsWanted').value.trim();
  let msg = document.getElementById('registerMsg');
  if (users.some(u => u.name.toLowerCase() === name.toLowerCase())) {
    msg.innerHTML = "<span class='error'>User already exists.</span>";
    return;
  }
  let skillsOffered = skillsOfferedInput.split(',').map(s => {
    let parts = s.split(':');
    return { name: (parts[0] || '').trim(), description: (parts[1] || '').trim() };
  }).filter(s => s.name);
  let skillsWanted = skillsWantedInput.split(',').map(s => s.trim()).filter(s => s);
  users.push({ name, discord, skillsOffered, skillsWanted });
  msg.innerHTML = "<span class='success'>User registered!</span>";
  saveUsers();
  setTimeout(() => { msg.innerHTML = ""; showAddSkillPage(); }, 1000);
};

document.getElementById('offerForm').onsubmit = function(e) {
  e.preventDefault();
  let name = document.getElementById('offerUser').value.trim();
  let skill = document.getElementById('offerSkill').value.trim();
  let desc = document.getElementById('offerDesc').value.trim();
  let msg = document.getElementById('offerMsg');
  let user = users.find(u => u.name.toLowerCase() === name.toLowerCase());
  if (user) {
    if (!user.skillsOffered.some(s => s.name.toLowerCase() === skill.toLowerCase())) {
      user.skillsOffered.push({ name: skill, description: desc });
      msg.innerHTML = "<span class='success'>Skill added!</span>";
      saveUsers();
      displayUsers();
      updateAutocomplete();
      this.reset();
    } else {
      msg.innerHTML = "<span class='error'>Skill already exists for this user.</span>";
    }
  } else {
    msg.innerHTML = "<span class='error'>User not found.</span>";
  }
  setTimeout(() => { msg.innerHTML = ""; }, 2000);
};

document.getElementById('wantForm').onsubmit = function(e) {
  e.preventDefault();
  let name = document.getElementById('wantUser').value.trim();
  let skill = document.getElementById('wantSkill').value.trim();
  let msg = document.getElementById('wantMsg');
  let user = users.find(u => u.name.toLowerCase() === name.toLowerCase());
  if (user) {
    if (!user.skillsWanted.includes(skill)) user.skillsWanted.push(skill);
    msg.innerHTML = "<span class='success'>Skill added!</span>";
    saveUsers();
    displayUsers();
    this.reset();
  } else {
    msg.innerHTML = "<span class='error'>User not found.</span>";
  }
  setTimeout(() => { msg.innerHTML = ""; }, 2000);
};

function updateAutocomplete() {
  let allSkills = [];
  users.forEach(u => u.skillsOffered.forEach(s => {
    if (!allSkills.includes(s.name)) allSkills.push(s.name);
  }));
  autocomplete(document.getElementById('searchSkill'), allSkills);
}

function autocomplete(inp, arr) {
  let currentFocus;
  inp.addEventListener("input", function(e) {
    let a, b, i, val = this.value;
    closeAllLists();
    if (!val) { return false;}
    currentFocus = -1;
    a = document.createElement("DIV");
    a.setAttribute("id", this.id + "autocomplete-list");
    a.setAttribute("class", "autocomplete-items");
    this.parentNode.appendChild(a);
    for (i = 0; i < arr.length; i++) {
      if (arr[i].toLowerCase().indexOf(val.toLowerCase()) !== -1) {
        b = document.createElement("DIV");
        b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
        b.innerHTML += arr[i].substr(val.length);
        b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
        b.addEventListener("click", function(e) {
            inp.value = this.getElementsByTagName("input")[0].value;
            closeAllLists();
        });
        a.appendChild(b);
      }
    }
  });
  inp.addEventListener("keydown", function(e) {
    let x = document.getElementById(this.id + "autocomplete-list");
    if (x) x = x.getElementsByTagName("div");
    if (e.keyCode == 40) {
      currentFocus++;
      addActive(x);
    } else if (e.keyCode == 38) {
      currentFocus--;
      addActive(x);
    } else if (e.keyCode == 13) {
      e.preventDefault();
      if (currentFocus > -1) {
        if (x) x[currentFocus].click();
      }
    }
  });
  function addActive(x) {
    if (!x) return false;
    removeActive(x);
    if (currentFocus >= x.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (x.length - 1);
    x[currentFocus].classList.add("autocomplete-active");
  }
  function removeActive(x) {
    for (let i = 0; i < x.length; i++) {
      x[i].classList.remove("autocomplete-active");
    }
  }
  function closeAllLists(elmnt) {
    let x = document.getElementsByClassName("autocomplete-items");
    for (let i = 0; i < x.length; i++) {
      if (elmnt != x[i] && elmnt != inp) {
        x[i].parentNode.removeChild(x[i]);
      }
    }
  }
  document.addEventListener("click", function (e) {
    closeAllLists(e.target);
  });
}

function searchSkill() {
  let skill = document.getElementById('searchSkill').value.trim();
  let resultDiv = document.getElementById('searchResult');
  let foundUsers = users.filter(user =>
    user.skillsOffered.some(s => s.name.toLowerCase() === skill.toLowerCase())
  );
  if (foundUsers.length > 0) {
    resultDiv.innerHTML = foundUsers.map(user =>
      `<div class="user">
        <b>${user.name}</b> (Discord: ${user.discord})<br>
        <strong>Offers:</strong> <ul>${user.skillsOffered.map(s => `<li>${s.name} - ${s.description}</li>`).join('')}</ul>
        <strong>Wants:</strong> ${user.skillsWanted.join(', ')}
      </div>`
    ).join('');
  } else {
    resultDiv.innerHTML = "<span class='error'>No users found offering that skill.</span>";
  }
}

document.getElementById('showAllUsersBtn').onclick = function() {
  showUsersPage();
};

document.getElementById('usersBackBtn').onclick = function() {
  showAddSkillPage();
};

document.getElementById('titleLink').onclick = function() {
  showAddSkillPage();
};
document.getElementById('titleLink2').onclick = function() {
  showAddSkillPage();
};
document.getElementById('titleLink3').onclick = function() {
  showAddSkillPage();
};

function handleHash() {
  if (location.hash === "#register") {
    showRegisterPage(false);
  } else if (location.hash === "#users") {
    showUsersPage(false);
  } else {
    showAddSkillPage(false);
  }
}

window.addEventListener('hashchange', handleHash);

loadUsers();
handleHash();
updateAutocomplete();