using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ModulusTreeCryptography
{
    public static class CryptographyUtilities
    {
        public static void EnsurePositive(ref byte[] bytes)
        {
            //if ((bytes[bytes.Length - 1] & 0x80) > 0)
            //{
                byte[] temp = new byte[bytes.Length];
                Array.Copy(bytes, temp, bytes.Length);
                bytes = new byte[temp.Length + 1];
                Array.Copy(temp, bytes, temp.Length);
            //}
        }

        public static void PadToMultipleOf(ref byte[] src, int pad)
        {
            int len = (src.Length + pad - 1) / pad * pad;
            Array.Resize(ref src, len);
        }
    }
}
