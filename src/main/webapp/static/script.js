// 🌐 TAB CONTROL

function showTab(tabNum) {
    // Hide all tab contents
    document.querySelectorAll('.tab-content').forEach((tab, i) => {
        tab.style.display = i === tabNum - 1 ? 'block' : 'none';
    });

    // Update active tab button
    document.querySelectorAll('.tab-button').forEach((btn, i) => {
        btn.classList.toggle('active', i === tabNum - 1);
    });

    // Tab 1 specific initialization
    if (tabNum === 1) {
        // Reset HTML dropdown to default "Select" option
        document.getElementById("htmlDropdown").selectedIndex = 0;

        // Small delay to ensure DOM is ready for Ext JS
        setTimeout(() => {
            initExtJsCombo();
        }, 100);
    }

    // Tab 3 specific initialization
    if (tabNum === 3) {
        currentPage = 0;
        loadData();
    }

    // Tab 4 specific initialization
    if (tabNum === 4) {
        loadPrefixList();
    }

    // Tab 6 specific initialization
    if (tabNum === 6) {
        loadLabels();
    }
}

function initExtJsCombo() {
    // Remove old instance if it exists
    var old = Ext.getCmp('extjsCombo');
    if (old) {
        old.destroy();
    }

    var container = Ext.get('extjsDropdownContainer');
    if (container) {
        container.dom.innerHTML = '';
    }

    // Create Ext JS ComboBox
    Ext.create('Ext.form.field.ComboBox', {
        id: 'extjsCombo',
        renderTo: 'extjsDropdownContainer',
        store: ['Select', 'IT', 'Accounts', 'HR', 'Management'],
        queryMode: 'local',
        editable: false,
        emptyText: 'Select',
        value: 'Select',
        width: 200,
        height: 34,
        hideLabel: true,
        labelAlign: 'top',
        style: {
            margin: '0px',
            padding: '0px'
        },
        fieldStyle: {
            'font-size': '14px',
            'height': '22px',
            'line-height': '22px'
        },
        cls: 'custom-combo',
        listeners: {
            afterrender: function(combo) {
                var el = combo.getEl();
                if (el) {
                    el.removeCls(['x-form-item-default']);
                }
            }
        }
    });
}

// Initialize when Ext JS is ready
Ext.onReady(function() {
    // Show tab 1 by default
    showTab(1);
    // Initialize the combo box
    initExtJsCombo();
});

// Fallback initialization
window.addEventListener('load', function() {
    setTimeout(function() {
        if (typeof Ext !== 'undefined' && Ext.isReady) {
            var existing = Ext.getCmp('extjsCombo');
            if (!existing) {
                initExtJsCombo();
            }
        }
    }, 500);
});

// 🧾 TAB 2: POPUP EDITOR

function openPopup() {
    document.getElementById("popup").style.display = "block";

    const summary = [
        document.getElementById("nameField").innerText,
        document.getElementById("mrnField").innerText,
        document.getElementById("dobField").innerText,
        document.getElementById("ageField").innerText,
        document.getElementById("genderField").innerText,
        document.getElementById("addressField").innerText,
        document.getElementById("regDateField").innerText,
        document.getElementById("statusField").innerText
    ].join(', ');

    document.getElementById("textEditor").innerText = summary;
}

function closePopup() {
    document.getElementById("popup").style.display = "none";
}



function format(command) {
    document.execCommand(command, false, null);
}

// 🔍 TAB 3: LIST + SEARCH + PAGINATION

let currentPage = 0;
let totalPages = 0;

document.addEventListener("DOMContentLoaded", () => {
    if (document.getElementById("searchInput")) {
        loadData();
        document.getElementById("searchInput").addEventListener("input", () => {
            currentPage = 0;
            loadData();
        });
    }
});

function loadData() {
    const search = document.getElementById("searchInput").value;

    fetch('api/patients?search=' + search + '&page=' + currentPage)
        .then(response => response.json())
        .then(data => {
            renderTable(data.patients);
            totalPages = data.totalPages;
            renderPagination();
        })
        .catch(error => console.error("Error fetching patients:", error));
}

function renderTable(patients) {
    const tableBody = document.getElementById("patientTableBody");
    tableBody.innerHTML = "";

    patients.forEach(patient => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${patient.name}</td>
            <td>${patient.age}</td>
            <td>${patient.gender}</td>
        `;
        tableBody.appendChild(row);
    });
}

function renderPagination() {
    const paginationDiv = document.getElementById("pagination");
    paginationDiv.innerHTML = "";

    for (let i = 0; i < totalPages; i++) {
        const btn = document.createElement("button");
        btn.textContent = i + 1;
        btn.classList.toggle("active", i === currentPage);
        btn.onclick = () => {
            currentPage = i;
            loadData();
        };
        paginationDiv.appendChild(btn);
    }
}

// 📋 TAB 4: PREFIX CRUD (DWR + Hibernate)

function savePrefix() {
    const prefix = {
        code: document.getElementById("code").value,
        description: document.getElementById("desc").value
    };

    PrefixService.create(prefix, function (response) {
        if (response[0] === "success") {
            alert(response[1]);
            loadPrefixList();
        } else {
            alert(response[1] || "Error saving");
        }
    });
}

function deletePrefix(id) {
    console.log("Attempting to delete prefix with ID:", id);
    PrefixService.deletePrefix(id, function (response) {
        console.log("Response from backend:", response);
        if (response[0] === "success") {
            alert(response[1]);
            loadPrefixList();
        } else {
            alert(response[1] || "Error deleting");
        }
    });
}

function loadPrefixList() {
    PrefixService.list(function (data) {
        const tbody = document.querySelector("#prefixTable tbody");
        tbody.innerHTML = "";

        data.forEach(p => {
            const row = `
                <tr>
                    <td>${p.id}</td>
                    <td>${p.code}</td>
                    <td>${p.description}</td>
                    <td><button onclick="deletePrefix(${p.id})">Delete</button></td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    });
}

// 📊 TAB 5: EXCEL UPLOAD/DOWNLOAD

window.downloadExcel = function () {
    window.location.href = "/PatientDetailSpringMVC_war/tab5/download";
};

document.getElementById('uploadForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const fileInput = this.elements['file'];
    if (!fileInput.files.length) return alert("Please choose a file");

    const formData = new FormData();
    formData.append("file", fileInput.files[0]);

    fetch("/PatientDetailSpringMVC_war/tab5/upload", {method: "POST", body: formData})
        .then(res => res.text())
        .then(msg => alert(msg))
        .catch(err => alert("Error uploading: " + err));
});

// 🏷️ TAB 6: WEB SERVICE CRUD

const baseUrl = "/PatientDetailSpringMVC_war/api/labels";

function createLabel() {
    const code = document.getElementById("labelCodeInput").value;
    const description = document.getElementById("labelDescInput").value;

    fetch(baseUrl, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "text/plain"
        },
        body: JSON.stringify({code, description})
    })
        .then(r => r.text())
        .then(msg => {
            alert(msg);
            clearForm();
            loadLabels();
        })
        .catch(err => alert("Error saving: " + err));
}

function loadLabels() {
    fetch(baseUrl, {
        headers: {"Accept": "application/json"}
    })
        .then(res => res.json())
        .then(data => {
            renderLabels(data.labels);
        })
        .catch(err => alert("Error fetching labels: " + err));
}

function renderLabels(labels) {
    const tbody = document.querySelector("#labelTable tbody");
    tbody.innerHTML = "";

    labels.forEach(label => {
        const row = `<tr>
            <td>${label.id}</td>
            <td>${label.code}</td>
            <td>${label.description}</td>
            <td>
                <button onclick="editLabel(${label.id}, '${label.code}', '${label.description}')">Edit</button>
                <button onclick="deleteLabel(${label.id})">Delete</button>
            </td>
        </tr>`;
        tbody.innerHTML += row;
    });
}

function deleteLabel(id) {
    if (!confirm("Are you sure you want to delete this?")) return;

    fetch(`${baseUrl}/${id}`, {
        method: "DELETE"
    })
        .then(res => res.text())
        .then(msg => {
            alert(msg);
            loadLabels();
        })
        .catch(err => alert("Error deleting: " + err));
}

function editLabel(id, code, description) {
    document.getElementById("labelIdInput").value = id;
    document.getElementById("labelCodeInput").value = code;
    document.getElementById("labelDescInput").value = description;

    document.getElementById("saveBtn").style.display = "none";
    document.getElementById("updateBtn").style.display = "inline-block";
}

function updateLabel() {
    const id = document.getElementById("labelIdInput").value;
    const code = document.getElementById("labelCodeInput").value;
    const description = document.getElementById("labelDescInput").value;

    fetch(baseUrl, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({id, code, description})
    })
        .then(res => res.text())
        .then(msg => {
            alert(msg);
            clearForm();
            loadLabels();
        })
        .catch(err => alert("Error updating: " + err));
}

function clearForm() {
    document.getElementById("labelIdInput").value = "";
    document.getElementById("labelCodeInput").value = "";
    document.getElementById("labelDescInput").value = "";
    document.getElementById("saveBtn").style.display = "inline-block";
    document.getElementById("updateBtn").style.display = "none";
}

// 📄 TAB 7: PDF GENERATION

function generatePdf() {
    fetch('/PatientDetailSpringMVC_war/api/tab7/generate-pdf')
        .then(response => response.text())
        .then(msg => alert(msg))
        .catch(err => alert('Error: ' + err));
}

// 🎯 DOCUMENT READY

document.addEventListener("DOMContentLoaded", function () {
    // Attach event to PDF button (Tab 7)
    const pdfBtn = document.getElementById("generatePdfBtn");
    if (pdfBtn) {
        pdfBtn.addEventListener("click", generatePdf);
    }
});