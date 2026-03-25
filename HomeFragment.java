package com.mibancolombia.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.mibancolombia.R;
import com.mibancolombia.activities.TransferActivity;
import com.mibancolombia.activities.PaymentActivity;
import com.mibancolombia.activities.ProductsActivity;

public class HomeFragment extends Fragment {

    private TextView balanceTextView;
    private TextView accountTypeTextView;
    private CardView transferCardView;
    private CardView paymentCardView;
    private CardView rechargeCardView;
    private CardView withdrawCardView;
    private CardView consultCardView;
    private CardView investCardView;
    private CardView requestCardView;
    private CardView promotionsCardView;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Referencias a los elementos de la UI
        balanceTextView = view.findViewById(R.id.balance_text_view);
        accountTypeTextView = view.findViewById(R.id.account_type_text_view);
        transferCardView = view.findViewById(R.id.transfer_card_view);
        paymentCardView = view.findViewById(R.id.payment_card_view);
        rechargeCardView = view.findViewById(R.id.recharge_card_view);
        withdrawCardView = view.findViewById(R.id.withdraw_card_view);
        consultCardView = view.findViewById(R.id.consult_card_view);
        investCardView = view.findViewById(R.id.invest_card_view);
        requestCardView = view.findViewById(R.id.request_card_view);
        promotionsCardView = view.findViewById(R.id.promotions_card_view);

        // Configurar listeners
        transferCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TransferActivity.class);
                startActivity(intent);
            }
        });

        paymentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                startActivity(intent);
            }
        });

        rechargeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar recarga
            }
        });

        withdrawCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar retiro sin tarjeta
            }
        });

        consultCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar consulta
            }
        });

        investCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar inversión
            }
        });

        requestCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar solicitud
            }
        });

        promotionsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implementar promociones
            }
        });

        // Simular datos de cuenta
        balanceTextView.setText("$5.234.567,89");
        accountTypeTextView.setText("Cuenta de Ahorros");

        return view;
    }
}