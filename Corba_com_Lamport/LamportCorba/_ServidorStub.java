package LamportCorba;


/**
* LamportCorba/_ServidorStub.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from Server.idl
* Quarta-feira, 29 de Abril de 2015 20h05min51s BRT
*/

public class _ServidorStub extends org.omg.CORBA.portable.ObjectImpl implements LamportCorba.Servidor
{


  //somente metodos que serao invocados remotamente
  public int entrarNoSD (org.omg.CORBA.Object servidor, String key)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("entrarNoSD", true);
                org.omg.CORBA.ObjectHelper.write ($out, servidor);
                $out.write_string (key);
                $in = _invoke ($out);
                int $result = $in.read_long ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return entrarNoSD (servidor, key        );
            } finally {
                _releaseReply ($in);
            }
  } // entrarNoSD


  //adiciona nova maquina ao SD
  public void updateSD (org.omg.CORBA.Object[] participantes, String[] chaves)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("updateSD", true);
                LamportCorba.ListaHelper.write ($out, participantes);
                LamportCorba.ListaSHelper.write ($out, chaves);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                updateSD (participantes, chaves        );
            } finally {
                _releaseReply ($in);
            }
  } // updateSD


  //quando novas maquinas sao adicionadas ao SD
  public void mensagemDoLider (int decisao)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("mensagemDoLider", true);
                $out.write_long (decisao);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                mensagemDoLider (decisao        );
            } finally {
                _releaseReply ($in);
            }
  } // mensagemDoLider

  public void mensagemDosMembros (int decisao)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("mensagemDosMembros", true);
                $out.write_long (decisao);
                $in = _invoke ($out);
                return;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                mensagemDosMembros (decisao        );
            } finally {
                _releaseReply ($in);
            }
  } // mensagemDosMembros

  public String requisicao_cliente (int requisicao)
  {
            org.omg.CORBA.portable.InputStream $in = null;
            try {
                org.omg.CORBA.portable.OutputStream $out = _request ("requisicao_cliente", true);
                $out.write_long (requisicao);
                $in = _invoke ($out);
                String $result = $in.read_string ();
                return $result;
            } catch (org.omg.CORBA.portable.ApplicationException $ex) {
                $in = $ex.getInputStream ();
                String _id = $ex.getId ();
                throw new org.omg.CORBA.MARSHAL (_id);
            } catch (org.omg.CORBA.portable.RemarshalException $rm) {
                return requisicao_cliente (requisicao        );
            } finally {
                _releaseReply ($in);
            }
  } // requisicao_cliente

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:LamportCorba/Servidor:1.0"};

  public String[] _ids ()
  {
    return (String[])__ids.clone ();
  }

  private void readObject (java.io.ObjectInputStream s) throws java.io.IOException
  {
     String str = s.readUTF ();
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     org.omg.CORBA.Object obj = orb.string_to_object (str);
     org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl) obj)._get_delegate ();
     _set_delegate (delegate);
   } finally {
     orb.destroy() ;
   }
  }

  private void writeObject (java.io.ObjectOutputStream s) throws java.io.IOException
  {
     String[] args = null;
     java.util.Properties props = null;
     org.omg.CORBA.ORB orb = org.omg.CORBA.ORB.init (args, props);
   try {
     String str = orb.object_to_string (this);
     s.writeUTF (str);
   } finally {
     orb.destroy() ;
   }
  }
} // class _ServidorStub