package cz.jaja.jajistina;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;

public class MainActivity extends AppCompatActivity
{
    private boolean direction = true;
    private TextView target;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button switcher = findViewById(R.id.button);
        final EditText source = findViewById(R.id.editText);
        target = findViewById(R.id.textView);

        switcher.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                direction = !direction;
                switcher.setText(direction ? "Lidi -> Jaja" : "Jaja -> Lidi");
                source.setText(target.getText());
            }
        });

        source.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                updateText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
            }
        });

        target.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", target.getText());
                clipboard.setPrimaryClip(clip);

                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Zkopirovano do schranky", Toast.LENGTH_SHORT);
                toast.show();

                return false;
            }
        });
    }

    private void updateText(String input)
    {
        if(direction)
        {
            target.setText(toJaja(input));
        }
        else
        {
            target.setText(fromJaja(input));
        }

    }

    private String toJaja(String input)
    {
        final StringBuilder binary = new StringBuilder();
        for (byte b : input.getBytes())
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                binary.append((val & 128) == 0 ? 'a' : 'J');
                val <<= 1;
            }
            binary.append(' ');
        }
        return binary.toString();
    }

    private String fromJaja(String input)
    {
        final String binary = input.replaceAll("J", "1").replaceAll("a", "0");
        return convertBinaryStringToString(binary);
    }

    private String convertBinaryStringToString(String string){
        char[] chars = string.toCharArray();
        char[] transcoded = new char[(chars.length / 9)+1];

        //for each character (plus one for spacing)
        for (int j = 0; j < chars.length; j+=9) {
            int idx = 0;
            int sum = 0;

            //for each bit in reverse
            for (int i = 7; i>= 0; i--) {
                if (chars[i+j] == '1') {
                    sum += 1 << idx;
                }
                idx++;
            }
            transcoded[j/9] = (char) sum;
        }
        return new String(transcoded);
    }
}