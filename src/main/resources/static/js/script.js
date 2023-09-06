document.addEventListener("DOMContentLoaded", function () {
    const filtroNomeInput = document.getElementById("filtroNome");
    const servicosTable = document.getElementById("servicosTable").getElementsByTagName("tbody")[0];

    filtroNomeInput.addEventListener("input", function () {
        const filtro = filtroNomeInput.value.toLowerCase();
        const rows = servicosTable.getElementsByTagName("tr");

        for (let i = 0; i < rows.length; i++) {
            const nomeServico = rows[i].getElementsByTagName("td")[0].textContent.toLowerCase();
            if (nomeServico.includes(filtro)) {
                rows[i].style.display = "";
            } else {
                rows[i].style.display = "none";
            }
        }
    });
});

// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyAsm_7gnB-XI-agSxPwLnY1hD9XYpV6l2U",
  authDomain: "goservice-squad3.firebaseapp.com",
  projectId: "goservice-squad3",
  storageBucket: "goservice-squad3.appspot.com",
  messagingSenderId: "124203117652",
  appId: "1:124203117652:web:2c6418dfe7566d0bf9d1c1",
  measurementId: "G-3NMGSNF1NH"
};

const app = firebase.initializeApp(firebaseConfig);
const storage = firebase.storage();


const uploadCompleto = document.querySelector(".uploadCompleto");

const fileData = document.querySelector(".filedata");
const urlimagem = document.querySelector("#urlImagem")
let file;
let fileName;
let uploadedFileName;

uploadCompleto.style.display = "none";
const getImageData = (e) => {
    file = e.target.files[0];
    fileName = Date.now() + file.name;
    if (fileName) {
        fileData.style.display = "block";
    }
    fileData.innerHTML = fileName;
    console.log(file, fileName);
};

    function showSpinner(flag = true){
        if (flag) {
            uploadCompleto.style.display = "block";

        } else {
           uploadCompleto.style.display = "none";
        }
    }
const uploadImage = () => {
    showSpinner();
    const storageRef = storage.ref().child("profilePics");
    const folderRef = storageRef.child(fileName);
    const uploadtask = folderRef.put(file);
    uploadtask.on(
        "state_changed",
        (snapshot) => {
            console.log("Snapshot", snapshot.ref.name);
            uploadedFileName = snapshot.ref.name;
        },
        (error) => {
            console.log(error);
        },
        () => {
            storage
                .ref("profilePics")
                .child(uploadedFileName)
                .getDownloadURL()
                .then((url) => {
                    showSpinner(false);
                    console.log("URL", url);
                    urlimagem.setAttribute("value", url);
                });
            console.log("File Uploaded Successfully");
        }
    );
};

