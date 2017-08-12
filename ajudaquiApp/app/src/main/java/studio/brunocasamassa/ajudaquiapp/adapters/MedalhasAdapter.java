package studio.brunocasamassa.ajudaquiapp.adapters;

/**
 * Created by bruno on 09/05/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import studio.brunocasamassa.ajudaquiapp.R;

public class MedalhasAdapter extends RecyclerView.Adapter {

    private ArrayList<Integer> badges;
    private Context context;

    public MedalhasAdapter(ArrayList<Integer> objects) {
        this.badges = objects;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        System.out.println("entreeeei");
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.model_badges, parent, false);
        MedalhasViewHolder holder = new MedalhasViewHolder(view);


        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MedalhasViewHolder medalhasHolder = (MedalhasViewHolder) holder;

        int badge = badges.get(position);
        System.out.println("entrei badge position " + position);



        switch (badge) {
            case 0:
                System.out.println("entrei badge " + badge);
                medalhasHolder.badgeView.setImageResource(R.drawable.badge_back);
                break;
            case 1:
                medalhasHolder.badgeView.setImageResource(R.drawable.medalhas_02);
                System.out.println("entrei badge " + badge);
                break;
            case 2:
                System.out.println("entrei badge " + badge);
                medalhasHolder.badgeView.setImageResource(R.drawable.medalhas_03);
                break;
            case 3:
                medalhasHolder.badgeView.setImageResource(R.drawable.medalhas_04);
                System.out.println("entrei badge " + badge);
                break;
            case 4:
                medalhasHolder.badgeView.setImageResource(R.drawable.medalhas_05);
                System.out.println("entrei badge " + badge);
                break;
            case 5:
                System.out.println("entrei badge " + badge);
                medalhasHolder.badgeView.setImageResource(R.drawable.medalhas_06);
                break;
            case 6:
                medalhasHolder.badgeView.setImageResource(R.drawable.medalhas_07);
                System.out.println("entrei badge " + badge);
                break;
            case 7:
                medalhasHolder.badgeView.setImageResource(R.drawable.medalhas_02);
                System.out.println("entrei badge " + badge);
                break;
            case 8:
                medalhasHolder.badgeView.setImageResource(R.drawable.medalhas_04);
                System.out.println("entrei badge " + badge);
                break;
            case 9:
                System.out.println("entrei badge " + badge);
                medalhasHolder.badgeView.setImageResource(R.drawable.medalhas_01);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return badges.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}


class MedalhasViewHolder extends RecyclerView.ViewHolder {


    final CircleImageView badgeView;

    public MedalhasViewHolder(View view) {
        super(view);

        // recupera elemento para exibição
        badgeView = (CircleImageView) view.findViewById(R.id.img_badge_id);


    }


}