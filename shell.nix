{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  nativeBuildInputs = with pkgs; [
    maven
    jdk21
  ];

  buildInputs = with pkgs; [
    xorg.libX11
    xorg.libXxf86vm
    xorg.libXtst
    libGL
    gtk3
    glib
  ];

  shellHook = ''
    export LD_LIBRARY_PATH=${pkgs.lib.makeLibraryPath [
      pkgs.xorg.libX11
      pkgs.xorg.libXxf86vm
      pkgs.xorg.libXtst
      pkgs.libGL
      pkgs.gtk3
      pkgs.glib
    ]}:$LD_LIBRARY_PATH
  '';
}